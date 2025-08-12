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
    public void migrate(MysqlConnDTO dto) {
        String url = "jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC"
                .formatted(dto.host(), dto.port(), dto.database());

        try (Connection conn = DriverManager.getConnection(url, dto.username(), dto.password());
             PreparedStatement userPs = conn.prepareStatement(
                     "SELECT uid, email, nickname, password,permission FROM users");
             PreparedStatement playersPs = conn.prepareStatement(
                     "SELECT players.name, tid_skin, tid_cape, uuid FROM players left join uuid on players.name = uuid.name where uid = ? ");
             PreparedStatement texturesPs = conn.prepareStatement(
                     "SELECT tid ,name, type, hash, likes,`public` FROM textures");
             PreparedStatement texturesUploaderPs = conn.prepareStatement(
                     "SELECT tid  FROM textures where uploader = ?");
             PreparedStatement closetPs = conn.prepareStatement(
                     "SELECT texture_tid  FROM user_closet where user_uid = ?");
             ) {
            ResultSet userRs = userPs.executeQuery();

            // 迁移材质
            migrateSkins(texturesPs.executeQuery());

            while (userRs.next()) {
                User user = migrateUser(userRs);

                // players处理
                playersPs.setString(1, userRs.getString("uid"));
                ResultSet playersRs = playersPs.executeQuery();
                migratePlayers(playersRs, user);


                texturesUploaderPs.setString(1, userRs.getString("uid"));
                migrateSkinsUploader(texturesUploaderPs.executeQuery(),user);

                closetPs.setString(1, userRs.getString("uid"));
                migrateCloset(closetPs.executeQuery(),  user);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        while (rs.next()) {
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
}
