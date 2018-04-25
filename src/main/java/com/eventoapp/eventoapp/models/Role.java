package com.eventoapp.eventoapp.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Role {

    @Id
    private String nomeRole;
    @ManyToMany(mappedBy = "roles")
    private List<Usuario> usuario;
}
