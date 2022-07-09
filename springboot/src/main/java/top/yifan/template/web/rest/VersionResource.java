package top.yifan.template.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "项目版本信息Api", description = "项目版本信息Api")
public class VersionResource {

    @Value("${env}")
    private String env;

    @GetMapping("/version")
    @ApiOperation("获取项目版本信息")
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok(env + " - 1.0.0");
    }
    
}