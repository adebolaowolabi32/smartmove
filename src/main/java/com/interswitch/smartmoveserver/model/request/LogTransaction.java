package com.interswitch.smartmoveserver.model.request;

import com.interswitch.smartmoveserver.model.Transaction;
import lombok.Data;

@Data
public class LogTransaction extends Transaction {
    private String messageId;
}
