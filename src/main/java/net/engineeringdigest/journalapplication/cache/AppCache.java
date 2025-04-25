package net.engineeringdigest.journalapplication.cache;

import jakarta.annotation.PostConstruct;
import net.engineeringdigest.journalapplication.entity.ConfigJournalAppEntry;
import net.engineeringdigest.journalapplication.repository.ConfigJournalAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys {
        WEATHER_API;
    }

    private final ConfigJournalAppRepository configJournalAppRepository;

    @Autowired
    public AppCache(ConfigJournalAppRepository configJournalAppRepository) {
        this.configJournalAppRepository = configJournalAppRepository;
    }

    public Map<String, String> appCache = new HashMap<>();

    @PostConstruct
    public  void init() {
        List<ConfigJournalAppEntry> all = configJournalAppRepository.findAll();
        for (ConfigJournalAppEntry entry : all) {
            appCache.put(entry.getKey(), entry.getValue());
        }
    }
}
