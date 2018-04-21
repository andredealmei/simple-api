package br.com.andredealmei.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

/**this class is used for authentication and authorization */

@Entity
public class User extends AbstractEntity {

    @NotEmpty
    @Column
    private String userName;

    @NotEmpty
    private String password;

    @NotEmpty /*used a simple boolean var for preserve the simplicity of application*/
    private boolean admin;

    String nome;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
