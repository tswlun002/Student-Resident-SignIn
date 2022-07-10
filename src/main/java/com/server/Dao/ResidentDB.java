package com.server.Dao;
import com.host.Host;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ResidentDB{
    /**
     * @serialField  hostList  - database of resident members
     */
    private final List<Host> hostList =
            List.of(new Host[]{
                    new Host(123, "Lunga", "0788148267", "C601C"),
                    new Host( 234,"Mpumelelo", "0688148267","B608B"),
                    new Host(345,"Sizamkele", "0888148267",  "C303C"),
                    new Host(456, "Nosipho", "0798148267", "B605B"),
                    new Host(567, "Sakhi", "0628148267", "C505C")
            });

    /**
     * Defaulted  Construct
     */
    public  ResidentDB(){}

    /**
     * @return List of the  members(Hosts) of the resident
     */
    public List<Host> getHostList() {
        return hostList;
    }
}
