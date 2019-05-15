package ru.sbrf.study.service.layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.study.service.dto.TokenAccManager;

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
    public void create(TokenAccManager tokenAccManager){
        try {
            businessService.registerAccount(tokenAccManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
