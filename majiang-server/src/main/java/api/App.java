package api;

import api.util.Util;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;


@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class }) // 排除自动注入数据源的配置
public class App implements CommandLineRunner{
	@Value("${data.path}")
	private String mDataPath;

	static {
		// 读取日志配置文件
		PropertyConfigurator.configure("log4j.properties");
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	@Override
	public void run(String... arg0) {
		Util.MA_JIANG_PATH = mDataPath;
		System.out.println(Util.MA_JIANG_PATH);
	}
}
