package ru.sbrf.study.service.layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.study.service.dto.AccountCreate;
import ru.sbrf.study.service.dto.AccountDelete;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.IOException;

@Service
@Path("accservice")
public class AccountService {

    private static final String APPLICATION_JSON = "application/json";

    @Autowired
    private BusinessService businessService;

    @POST
    @Path("create")
    @Consumes(APPLICATION_JSON)
    public void createAccount(AccountCreate accountCreate){
        try {
            businessService.registerAccount(accountCreate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("delete")
    @Consumes(APPLICATION_JSON)
    public void deleteAccount(AccountDelete accountDelete){
        //метод удаления счета
    }

    @POST
    @Path("pull")
    @Consumes(APPLICATION_JSON)
    public void pullMoney(AccountDelete accountDelete){
        //метод снятия денег со счета
    }

    @POST
    @Path("push")
    @Consumes(APPLICATION_JSON)
    public void pushMoney(AccountDelete accountDelete){
        //метод снятия денег со счета
    }
}
