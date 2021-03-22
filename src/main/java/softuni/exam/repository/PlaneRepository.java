package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.Plane;

@Repository
public interface PlaneRepository extends JpaRepository<Plane, Integer> {

    Plane findByRegisterNumber(String registerNumber);

}
