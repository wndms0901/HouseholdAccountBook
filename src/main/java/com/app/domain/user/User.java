package com.app.domain.user;

import com.app.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "user_email")
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(insertable=false, nullable = false, columnDefinition = "varchar(30) default '1'")
    private String monthStartDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

//    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//    private List<Role> roles = new ArrayList<Role>();

    @OneToMany(mappedBy = "user")
    private List<Expenditure> expenditureList = new ArrayList<Expenditure>();

    @OneToMany(mappedBy = "user")
    private List<Budget> budgetList = new ArrayList<Budget>();

    @OneToMany(mappedBy = "user")
    private List<Income> incomeList = new ArrayList<Income>();

//    @OneToMany(mappedBy = "user")
//    private List<IncomeBudget> incomeBudgetList = new ArrayList<IncomeBudget>();

    @Builder
    public User(String name, String email, String password, String monthStartDate, Role role) {
        this.name = name;
        this.email = email;
        this.password= password;
        this.monthStartDate = monthStartDate;
        this.role = role;
    }

    public User update(String name, String monthStartDate) {
        this.name = name;
        this.monthStartDate = monthStartDate;
        return this;
    }

    public void updateMonthStartDate(String monthStartDate){
        this.monthStartDate = monthStartDate;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

//    public List<String> getRoleList(){
//        List<String> roleList = new ArrayList<>();
//        for(Role r: roles){
//            roleList.add(r.getRoleName().getKey());
//        }
//        return roleList;
//    }

    public List<Expenditure> getExpenditureList() {
        return expenditureList;
    }

    public List<Budget> getBudgetList() {
        return budgetList;
    }

    public List<Income> getIncomeList() {
        return incomeList;
    }

//    public List<IncomeBudget> getIncomeBudgetList() {
//        return incomeBudgetList;
//    }

    public User encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
        return this;
    }


}
