package com.dkd.test;

import com.dkd.generator.util.VelocityInitializer;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.FileWriter;
import java.util.List;

public class VelocityDemoTest {

    public static void main(String[] args) throws Exception{
        //1. 初始化模板引擎
        VelocityInitializer.initVelocity();
        //2. 准备模板数据模型
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("message", "加油少年！！");
        // 创建区域对象
        Region region1 = new Region(1L, "北京北五环");
        Region region2 = new Region(2L, "北京北四环");
        velocityContext.put("region",region1);
        List<Region> list = List.of(region1, region2);
        velocityContext.put("regionList",list);
        //3. 读取模板
        Template template = Velocity.getTemplate("vm/index.html.vm", "UTF-8");
        //4. 渲染模板（合并输出）
        FileWriter fileWriter = new FileWriter("D:\\workspace\\index.html");// 输出到文件
        template.merge(velocityContext, fileWriter);
        fileWriter.close();// 关闭流
    }
}
