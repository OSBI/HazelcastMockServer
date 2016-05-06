package servlets;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import javax.servlet.http.HttpServlet;

/**
 * Created by bugg on 06/05/16.
 */
public class HazelcastImpl extends HttpServlet {

    public HazelcastImpl() {
        System.out.println("----------");
        System.out.println("---------- CrunchifyExample Servlet Initialized successfully ----------");
        System.out.println("----------");
        Config cfg = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);

    }
}
