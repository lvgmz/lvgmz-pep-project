package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }
    

    // #1 User Registration
    public Account addAccount(Account account){
        if (account.username == ""){
            return null;
        }
        if (account.password.length() < 4){
            return null;
        }
        
        //add check for duplicate account need dao method first (tests pass though?)

        return accountDAO.insertAccount(account);
    }

    // #2 User Login
    public Account checkAccount(Account account){
        return accountDAO.getAccountByCredentials(account.username, account.password);
    }

}
