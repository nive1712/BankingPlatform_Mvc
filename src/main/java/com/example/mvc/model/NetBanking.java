package com.example.mvc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "net_banking")
public class NetBanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int netBankingId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    private String password;

    public NetBanking() {
    	
    }

    public int getNetBankingId() {
        return netBankingId;
    }

    public void setNetBankingId(int netBankingId) {
        this.netBankingId = netBankingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
