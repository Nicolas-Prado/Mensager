package persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import vo.Mensageiro;

public class MensageiroDAO {

    EntityManager em;

    public MensageiroDAO() {
        em = EntityManagerProvider.getEM();
    }

    public void salva(Mensageiro p) {
        em.getTransaction().begin();
        if (p.getId() == 0) {
            em.persist(p);
        } else {
            em.merge(p);
        }
        em.getTransaction().commit();
    }

    public Mensageiro localiza(String login) {
        Mensageiro p = em.find(Mensageiro.class, login);
        return p;
    }

    public void exclui(Mensageiro p) {
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }

    public List<Mensageiro> pesquisa() {
        Query q = em.createQuery("select p from Mensageiro p order by p.id");
        List<Mensageiro> lista = q.getResultList();
        return lista;
    }
    
    public List<Mensageiro> mensagensusu(String dest){
        Query q = em.createQuery("select p from Mensageiro p where p.destinatario= :destinatario order by p.dataa desc");
        q.setParameter("destinatario", dest);
        List<Mensageiro> lista = q.getResultList();
        return lista;
    }
    
    public List<Mensageiro> recebidausu(String rem){
        Query q = em.createQuery("select p from Mensageiro p where p.remetente= :rem order by p.dataa desc");
        q.setParameter("rem", rem);
        List<Mensageiro> lista = q.getResultList();
        return lista;
    }
}
