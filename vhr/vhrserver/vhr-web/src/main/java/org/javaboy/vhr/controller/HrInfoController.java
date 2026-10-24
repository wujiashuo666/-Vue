package org.javaboy.vhr.controller;

import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;


@RestController
public class HrInfoController {

    @Autowired
    HrService hrService;

    // 头像存到前端 public/avatar 目录，Vite 会直接服务
    private static final String AVATAR_DIR = "D:/mycode/Github1/vhr/vuehr/public/avatar/";

    @GetMapping("/hr/info")
    public Hr getCurrentHr(Authentication authentication) {
        //principal 是登录时的快照，头像等资料变更后不会更新，
        //这里从数据库实时查询，保证前端刷新页面即可看到最新资料
        Hr current = (Hr) authentication.getPrincipal();
        Hr hr = hrService.getHrById(current.getId());
        //密码哈希不能返回给前端
        hr.setPassword(null);
        return hr;
    }

    @PutMapping("/hr/info")
    public RespBean updateHr(@RequestBody Hr hr, Authentication authentication) {
        if (hrService.updateHr(hr) == 1) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(hr, authentication.getCredentials(), authentication.getAuthorities()));
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @PutMapping("/hr/pass")
    public RespBean updateHrPasswd(@RequestBody Map<String, Object> info) {
        String oldpass = (String) info.get("oldpass");
        String pass = (String) info.get("pass");
        Integer hrid = (Integer) info.get("hrid");
        if (hrService.updateHrPasswd(oldpass, pass, hrid)) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @PostMapping("/hr/userface")
    public RespBean updateHrUserface(MultipartFile file, Integer id, Authentication authentication) {
        try {
            // 确保目录存在
            File dir = new File(AVATAR_DIR);
            if (!dir.exists()) dir.mkdirs();
            // 生成唯一文件名
            String original = file.getOriginalFilename();
            String ext = original != null && original.contains(".") ? original.substring(original.lastIndexOf(".")) : ".jpg";
            String filename = UUID.randomUUID().toString() + ext;
            // 保存文件
            Path path = Paths.get(AVATAR_DIR + filename);
            Files.write(path, file.getBytes());
            // 数据库存访问路径（前端 Vite 代理会把 /avatar 映射到 public 目录）
            String url = "/avatar/" + filename;
            if (hrService.updateUserface(url, id) == 1) {
                Hr hr = (Hr) authentication.getPrincipal();
                hr.setUserface(url);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(hr, authentication.getCredentials(), authentication.getAuthorities()));
                return RespBean.ok("更新成功!", url);
            }
            return RespBean.error("更新失败!");
        } catch (IOException e) {
            e.printStackTrace();
            return RespBean.error("上传失败: " + e.getMessage());
        }
    }

}