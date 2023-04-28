package is.tech.entities;

import lombok.NonNull;
import is.tech.exceptions.BankException;
import is.tech.models.ClientAddress;
import is.tech.models.ClientName;
import is.tech.models.ClientPassportId;

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
