package com.interswitch.smartmoveserver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Wallet;
import com.interswitch.smartmoveserver.service.WalletService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = WalletApi.class)
public class WalletApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private WalletService walletService;

    private Wallet wallet;

    @BeforeAll
    public void setup() {
        wallet = new Wallet();
        long id = 10000023;
        wallet.setId(id);
        wallet.setOwner(new User());
        wallet.setBalance(200000);
        wallet.setEnabled(true);
    }

    @Test
    public void testSave() throws Exception {
        when(walletService.save(wallet)).thenReturn(wallet);
        mvc.perform(post("/wallets")
                .content(new ObjectMapper().writeValueAsString(wallet))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testUpdate() throws Exception {
        when(walletService.update(wallet)).thenReturn(wallet);
        mvc.perform(put("/wallets")
                .content(new ObjectMapper().writeValueAsString(wallet))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testGetFindById() throws Exception {
        when(walletService.findById(wallet.getId())).thenReturn(wallet);
        mvc.perform(get("/wallets/{id}", wallet.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/wallets/{id}", wallet.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
