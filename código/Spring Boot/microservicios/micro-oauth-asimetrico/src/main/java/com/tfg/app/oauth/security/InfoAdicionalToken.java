package com.tfg.app.oauth.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.tfg.app.comusuarios.modelos.entidades.Usuario;
import com.tfg.app.oauth.servicios.IUsuarioServicio;

@Component
public class InfoAdicionalToken implements TokenEnhancer {

	@Autowired
	private IUsuarioServicio usuarioService;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);

		Map<String, Object> info = new HashMap<String, Object>();

		System.out.println(authentication.getName());
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		info.put("id", usuario.getId());
		info.put("fechaRegistro", usuario.getFechaRegistro());
		//asi machaca ese objeto raro que ni es array ni es objeto que aparece en el json en el frontend
		info.put("rol", usuario.getRol().getId());
		
		token.setAdditionalInformation(info);

		return token;
	}

}