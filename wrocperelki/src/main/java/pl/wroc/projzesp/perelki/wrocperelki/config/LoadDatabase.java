package pl.wroc.projzesp.perelki.wrocperelki.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.wroc.projzesp.perelki.wrocperelki.data.Miejsce;
import pl.wroc.projzesp.perelki.wrocperelki.data.Zagadka;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.MiejsceRepository;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.ZagadkaRepository;

@Configuration
class LoadDatabase {
    @Bean
    CommandLineRunner initZagadki(ZagadkaRepository repository) {

        return args -> {
            repository.save(new Zagadka("łatwe","Wysoka woda","Wieże ciśnień","teraz już wiesz gdzie są wieże ciśnień"));
            repository.save(new Zagadka("trudne","Zielony płaz","Sklepy żabka","teraz już wiesz gdzie jest najbliższa żabka"));
        };
    }
    @Bean
    CommandLineRunner initMiejsca(MiejsceRepository miejsca, ZagadkaRepository repository) {

        return args -> {
            miejsca.save(new Miejsce("Wieża Ciśnień Na Grobli", "51.104556, 17.057833", "10m","https://cdn.img.wroclaw.pl/api/download/img-529d4c812df7bd084e16842978926a8d/wieza-ma-panorama-jpg.jpg", "51.104556, 17.057833 ?", "0,-22", "https://pl.wikipedia.org/wiki/Wie%C5%BCa_ci%C5%9Bnie%C5%84_Na_Grobli",repository.getReferenceById(1L)));
            miejsca.save(new Miejsce("Wieża ciśnień Borek przy Al. Wiśniowej","51.085278,17.0175", "12m", "https://wiezecisnien.eu/wp-content/uploads/2016/01/Wroclaw_poczt.jpg", "51.085278,17.0175 ?", "0,1", "https://pl.wikipedia.org/wiki/Wie%C5%BCa_ci%C5%9Bnie%C5%84_Borek_we_Wroc%C5%82awiu", repository.getReferenceById(1L)));
        };
    }
}
