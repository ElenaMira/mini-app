package cn.iocoder.boot.springsecurity;


import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("cn.iocoder.boot.springsecurity.**.dal.mysql")
@SpringBootApplication(scanBasePackages = {"cn.iocoder.boot.springsecurity"})
public class SpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

}
