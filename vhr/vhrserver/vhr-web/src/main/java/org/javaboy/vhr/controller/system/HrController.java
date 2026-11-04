package org.javaboy.vhr.controller.system;

import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.Role;
import org.javaboy.vhr.service.HrService;
import org.javaboy.vhr.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/system/hr")
public class HrController {
    @Autowired
    HrService hrService;
    @Autowired
    RoleService roleService;
    @GetMapping("/")
    public List<Hr> getAllHrs(String keywords) {
        return hrService.getAllHrs(keywords);
    }

    /**
     * 手动新增操作员：登录名唯一校验、初始密码 BCrypt 加密，可选一并分配角色
     */
    @PostMapping("/")
    public RespBean addHr(@RequestBody Hr hr) {
        return hrService.addHr(hr);
    }

    @PutMapping("/")
    public RespBean updateHr(@RequestBody Hr hr) {
        if (hrService.updateHr(hr) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/role")
    public RespBean updateHrRole(Integer hrid, Integer[] rids) {
        if (hrService.updateHrRole(hrid, rids)) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteHrById(@PathVariable Integer id) {
        if (hrService.deleteHrById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    // 头像存到前端 public/avatar 目录，与个人中心 /hr/userface 同一存储位置
    private static final String AVATAR_DIR = "D:/mycode/Github1/vhr/vuehr/public/avatar/";

    /**
     * 管理员为指定操作员更换头像（uid 为目标操作员 id）。
     * 与 /hr/userface 不同：这里不修改当前登录人的 SecurityContext。
     */
    @PostMapping("/userface")
    public RespBean updateHrUserface(MultipartFile file, Integer uid) {
        if (uid == null || file == null || file.isEmpty()) {
            return RespBean.error("参数不完整!");
        }
        try {
            File dir = new File(AVATAR_DIR);
            if (!dir.exists()) dir.mkdirs();
            String original = file.getOriginalFilename();
            String ext = original != null && original.contains(".") ? original.substring(original.lastIndexOf(".")) : ".jpg";
            String filename = UUID.randomUUID().toString() + ext;
            Path path = Paths.get(AVATAR_DIR + filename);
            Files.write(path, file.getBytes());
            String url = "/avatar/" + filename;
            if (hrService.updateUserface(url, uid) == 1) {
                return RespBean.ok("更新成功!", url);
            }
            return RespBean.error("更新失败!");
        } catch (IOException e) {
            e.printStackTrace();
            return RespBean.error("上传失败: " + e.getMessage());
        }
    }
}
