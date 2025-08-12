package online.yudream.yudreamskin.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import online.yudream.yudreamskin.common.enums.SystemRole;
import online.yudream.yudreamskin.entity.*;
import online.yudream.yudreamskin.entity.dto.MysqlConnDTO;
import online.yudream.yudreamskin.mapper.ClosetMapper;
import online.yudream.yudreamskin.mapper.GameProfileMapper;
import online.yudream.yudreamskin.mapper.SkinMapper;
import online.yudream.yudreamskin.mapper.UserMapper;
import online.yudream.yudreamskin.service.MigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class MigrationServiceImpl implements MigrationService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private GameProfileMapper gameProfileMapper;
    @Resource
    private SkinMapper skinMapper;
    @Autowired
    private ClosetMapper closetMapper;

    @Override
    public void migrate(MysqlConnDTO dto, PrintWriter w) {
        String url = "jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC"
                .formatted(dto.host(), dto.port(), dto.database());

        try (Connection conn = DriverManager.getConnection(url, dto.username(), dto.password())) {

            /* ---------- 阶段 1：统计总数 ---------- */
            log(w, COLOR_CYAN, "🔍 正在统计记录数...");
            int userTotal, textureTotal;
            try (Statement st = conn.createStatement()) {
                try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users")) {
                    rs.next(); userTotal = rs.getInt(1);
                }
                try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM textures")) {
                    rs.next(); textureTotal = rs.getInt(1);
                }
            }
            log(w, COLOR_GREEN, "📊 总览：用户 " + userTotal + "  | 材质 " + textureTotal);

            /* ---------- 阶段 2：迁移材质（子进度条） ---------- */
            log(w, COLOR_CYAN, "🖼️ 开始迁移材质...");
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT tid, name, type, hash, likes, public FROM textures");
                 ResultSet rs = ps.executeQuery()) {

                int migrated = 0;
                while (rs.next()) {
                    migrateSkins(rs); // 你的原逻辑
                    migrated++;
                    progressBar(w, migrated, textureTotal);
                }
            }

            /* ---------- 阶段 3：迁移用户（主进度条） ---------- */
            log(w, COLOR_CYAN, "👤 开始迁移用户...");
            try (PreparedStatement userPs = conn.prepareStatement(
                    "SELECT uid, email, nickname, password, permission FROM users");
                 ResultSet userRs = userPs.executeQuery()) {

                int migrated = 0;
                while (userRs.next()) {
                    User user = migrateUser(userRs);               // 你的原逻辑
                    migrateUserRelations(conn, user, userRs.getString("uid"), w);           // 下面封装
                    migrated++;
                    progressBar(w, migrated, userTotal);
                }
            }

            log(w, COLOR_GREEN, "🎉 迁移全部完成！");
        } catch (SQLException e) {
            e.printStackTrace();
            log(w, COLOR_RED, "❌ 失败：" + e.getMessage());
        }
    }

    /* 把每个用户相关的三张表一次性迁移，同时输出子进度 */
    private void migrateUserRelations(Connection conn, User user, String uid,PrintWriter w) throws SQLException {
        log(w, COLOR_YELLOW, "└ 处理用户 " + user.getNickname());
        log(w, COLOR_YELLOW, "└ - 处理profile中... " + user.getNickname());
        /* players */
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT players.name, tid_skin, tid_cape, uuid FROM players left join uuid on players.name = uuid.name where uid = ? ")) {
            ps.setString(1, uid);
            migratePlayers(ps.executeQuery(), user);
        }
        log(w, COLOR_YELLOW, "└ - 处理texturesUploader中... " + user.getNickname());

        /* texturesUploader */
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT tid FROM textures WHERE uploader = ?")) {
            ps.setString(1, uid);
            migrateSkinsUploader(ps.executeQuery(), user);
        }
        log(w, COLOR_YELLOW, "└ - 处理closet中... " + user.getNickname());
        /* closet */
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT texture_tid FROM user_closet WHERE user_uid = ?")) {
            ps.setString(1, uid);
            migrateCloset(ps.executeQuery(), user);
        }
    }

    private User migrateUser(ResultSet rs)  throws SQLException {
        Role role = new Role();
        switch (rs.getInt("permission")) {
            case 2:
                role.setId(SystemRole.SUPER_ADMIN.getRole().getId());
                break;
            case 1:
                role.setId(SystemRole.ADMIN.getRole().getId());
                break;
            default:
                role.setId(SystemRole.USER.getRole().getId());
        }
        User dbUser = userMapper.findUserByUsernameOrEmail(rs.getString("nickname"), rs.getString("email"));
        User user = User.builder()
                .email(rs.getString("email"))
                .nickname(rs.getString("nickname"))
                .username(rs.getString("nickname"))
                .password(rs.getString("password"))
                .roles(List.of(role))
                .build();
        if (dbUser != null && user.getEmail().equals(dbUser.getEmail())) {
            return dbUser;
        }
        if (dbUser != null) {
            user.setUsername(rs.getString("nickname") + user.getEmail().substring(0,3));
        }
        user = userMapper.save(user);
        return user;
    }

    private void migratePlayers(ResultSet rs, User user) throws SQLException {
        while (rs.next()) {
            Skin skin = skinMapper.findSkinByMigratedId(rs.getInt("tid_skin"));
            Skin cape = skinMapper.findSkinByMigratedId(rs.getInt("tid_cape"));
            GameProfile gameProfile = GameProfile.builder()
                    .user(user)
                    .uuid(rs.getString("uuid"))
                    .name(rs.getString("name"))
                    .cape(cape)
                    .skin(skin)
                    .build();
            User dbGameProfile = gameProfileMapper.findGameProfileByName(gameProfile.getName());
            if (dbGameProfile == null) {
                gameProfileMapper.save(gameProfile);
            }
        }
    }

    private void migrateSkins(ResultSet rs) throws SQLException {
            Skin skin = Skin.builder()
                    .name(rs.getString("name"))
                    .migratedId(rs.getInt("tid"))
                    .hash(rs.getString("hash"))
                    .like(rs.getInt("likes"))
                    .status(rs.getInt("public")==0?1:0)
                    .skinType(Objects.equals(rs.getString("type"), "cape") ?"cape": "skin")
                    .build();
            if (skin.getSkinType().equals("skin")) {
                String type = rs.getString("type");
                if (type.equals("alex")) {
                    skin.setMetadata(Map.of("model","slim"));
                } else {
                    skin.setMetadata(Map.of("model","default"));
                }
            }
            skinMapper.save(skin);

    }

    private void migrateSkinsUploader(ResultSet rs,User user) throws SQLException {
        while (rs.next()) {
            Skin skin = skinMapper.findSkinByMigratedId(rs.getInt("tid"));
            skin.setUser(user);
            skinMapper.save(skin);
        }
    }

    private void migrateCloset(ResultSet rs, User user) throws SQLException {
        while (rs.next()) {
            Closet closet = new Closet();
            Skin skin = skinMapper.findSkinByMigratedId(rs.getInt("texture_tid"));
            closet.setSkin(skin);
            closet.setUser(user);
            closetMapper.save(closet);
        }
    }

    private static final String COLOR_RESET = "</span>";
    private static final String COLOR_GREEN = "<span style='color:#CCD1B2'>";
    private static final String COLOR_YELLOW = "<span style='color:#D9C87C'>";
    private static final String COLOR_CYAN = "<span style='color:#00ffff'>";
    private static final String COLOR_RED = "<span style='color:#ff0000'>";

    private void log(PrintWriter w, String color, String msg) {
        w.println(color + msg + COLOR_RESET + "<br>");
        w.flush();
    }

    private void progressBar(PrintWriter w, int current, int total) {
        if (total == 0) return;
        int percent = (int) (current * 100.0 / total);
        int barLen = 40;
        int filled = (int) (barLen * percent / 100.0);
        String bar = "█".repeat(filled) + "░".repeat(barLen - filled);
        w.printf("\r%s[%-40s] %3d%% %s<br>", COLOR_GREEN, bar, percent , COLOR_RESET);
        w.flush();
    }
}
