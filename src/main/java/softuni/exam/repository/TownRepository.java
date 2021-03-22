package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.Town;

@Repository
public interface TownRepository extends JpaRepository<Town, Integer> {

    Town findByName(String townName);

}
