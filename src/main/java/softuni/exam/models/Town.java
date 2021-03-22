package softuni.exam.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity {

    private String name;
    private int population;
    private String guide;
    private Set<Passenger> passengers;
    private Set<Ticket> ticketsFromTown;
    private Set<Ticket> ticketsToTown;

    public Town() {
    }

    @Column(unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Column
    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    @OneToMany(mappedBy = "town")
    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    @OneToMany(mappedBy = "fromTown")
    public Set<Ticket> getTicketsFromTown() {
        return ticketsFromTown;
    }

    public void setTicketsFromTown(Set<Ticket> tickets) {
        this.ticketsFromTown = tickets;
    }

    @OneToMany(mappedBy = "toTown")
    public Set<Ticket> getTicketsToTown() {
        return ticketsToTown;
    }

    public void setTicketsToTown(Set<Ticket> ticketsToTown) {
        this.ticketsToTown = ticketsToTown;
    }
}
