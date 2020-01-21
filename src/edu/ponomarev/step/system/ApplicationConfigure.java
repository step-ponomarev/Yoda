package edu.ponomarev.step.system;

import edu.ponomarev.step.MVC.controller.Controller;
import edu.ponomarev.step.MVC.model.Worker;
import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("edu.ponomarev.step")
@PropertySource("classpath:placeholders")
public class ApplicationConfigure {
  @Bean
  @Scope("singleton")
  public RepositoryFactory repositoryFactory() {
    return ( new RepositoryFactory() );
  }

  @Bean
  @Scope("singleton")
  public Controller controller() {
    return ( new Controller() );
  }

  @Bean
  @Scope("singleton")
  public Worker worker() { return ( new Worker()); }
}
