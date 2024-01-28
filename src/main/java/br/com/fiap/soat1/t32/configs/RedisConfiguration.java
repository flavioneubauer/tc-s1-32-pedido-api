package br.com.fiap.soat1.t32.configs;

import br.com.fiap.soat1.t32.enums.CategoriaProduto;
import br.com.fiap.soat1.t32.models.entities.pedidos.Produto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

@Configuration
public class RedisConfiguration {

	@Bean
	LettuceConnectionFactory connectionFactory() {
		return new LettuceConnectionFactory();
	}
	@Bean
	RedisTemplate<CategoriaProduto, Set<Produto>> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<CategoriaProduto, Set<Produto>> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		return template;
	}
}
