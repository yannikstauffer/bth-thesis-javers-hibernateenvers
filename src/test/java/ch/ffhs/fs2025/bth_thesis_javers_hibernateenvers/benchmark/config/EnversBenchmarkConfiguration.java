package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(
        basePackages = "ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX,
                        pattern = "ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers..*"),
                @ComponentScan.Filter(type = FilterType.REGEX,
                        pattern = "ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers..*"),
                @ComponentScan.Filter(type = FilterType.REGEX,
                        pattern = "ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.rest..*")
        }
)
@EntityScan(basePackages = "ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers")
@EnableAutoConfiguration(exclude = {JpaRepositoriesAutoConfiguration.class, WebMvcAutoConfiguration.class})
@EnableJpaRepositories(value = "ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers")
public class EnversBenchmarkConfiguration {
}
