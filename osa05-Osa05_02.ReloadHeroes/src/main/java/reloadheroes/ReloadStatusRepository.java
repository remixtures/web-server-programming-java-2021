package reloadheroes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReloadStatusRepository extends JpaRepository<ReloadStatus, Long> {

    ReloadStatus findByName(String name);

}
