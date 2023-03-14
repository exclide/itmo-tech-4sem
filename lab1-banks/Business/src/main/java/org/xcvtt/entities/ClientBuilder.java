package org.xcvtt.entities;

import lombok.NonNull;
import org.xcvtt.exceptions.BankException;
import org.xcvtt.models.ClientAddress;
import org.xcvtt.models.ClientName;
import org.xcvtt.models.ClientPassportId;

public class ClientBuilder {
    private ClientName clientName;
    private ClientAddress clientAddress;
    private ClientPassportId clientPassportId;

    public ClientBuilder SetClientName(@NonNull ClientName clientName)
    {
        this.clientName = clientName;
        return this;
    }

    public ClientBuilder SetClientAddress(@NonNull ClientAddress clientAddress)
    {
        this.clientAddress = clientAddress;
        return this;
    }

    public ClientBuilder SetClientPassportId(@NonNull ClientPassportId clientPassportId)
    {
        this.clientPassportId = clientPassportId;
        return this;
    }

    public Client GetClient(int clientId)
    {
        if (clientName == null)
        {
            throw new BankException("Client name was null");
        }

        return new Client(clientName, clientAddress, clientPassportId, clientId);
    }
}
