package com.example.museo_v2.service;

import com.example.museo_v2.model.Usuario;
import com.example.museo_v2.repository.UsuarioRepositorio; 

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class) 
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Captor
    ArgumentCaptor<Usuario> usuarioArgumentCaptor;

    @Test
    void crearUsuario_DebeEncriptarClaveAntesDeGuardar() {
        
        String clavePlana = "miClaveSecreta123";

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("testUser");
        usuario.setNombres("Usuario de Prueba");
        usuario.setClave(clavePlana); 

        when(usuarioRepositorio.save(any(Usuario.class))).thenReturn(usuario);

       
        Usuario usuarioGuardado = usuarioService.crearUsuario(usuario);


        verify(usuarioRepositorio, times(1)).save(usuarioArgumentCaptor.capture());

        Usuario usuarioCapturado = usuarioArgumentCaptor.getValue();

        assertNotNull(usuarioGuardado);

        assertNotEquals(clavePlana, usuarioCapturado.getClave());

        assertTrue(usuarioCapturado.getClave().startsWith("$2a$"));
    }
}