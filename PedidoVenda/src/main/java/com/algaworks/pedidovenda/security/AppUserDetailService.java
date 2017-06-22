package com.algaworks.pedidovenda.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.UsuarioRepository;
import com.algaworks.pedidovenda.util.cdi.CDIServiceLocator;

public class AppUserDetailService implements UserDetailsService {

	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UsuarioRepository repository = CDIServiceLocator.getBean(UsuarioRepository.class);
		Usuario usuario = repository.porEmail(email);

		UserDetails userDetails = null;

		if (usuario.isNotNull()) {
			Collection<? extends GrantedAuthority> grupos = getGruposDe(usuario);
			
			userDetails = new UsuarioSistema(usuario, grupos);
		}

		return userDetails;
	}

	private Collection<? extends GrantedAuthority> getGruposDe(Usuario usuario) {
		List<SimpleGrantedAuthority> permissoes = new ArrayList<SimpleGrantedAuthority>();
		
		for(Grupo grupo : usuario.getGrupos()){
			String nome = grupo.getNome().toUpperCase();
			permissoes.add(new SimpleGrantedAuthority(nome));
		}
		
		return permissoes;
	}

}
