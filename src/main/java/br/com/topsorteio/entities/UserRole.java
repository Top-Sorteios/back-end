package br.com.topsorteio.entities;

public enum UserRole {
    USER(0),
    ADMIN(1);
    private int role;

    UserRole(int role){
        this.role = role;
    }

    public int getRole(){
        return role;
    }

}
