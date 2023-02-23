package persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import vo.Usuario;

public class UsuarioDAO {

    EntityManager em;

    public UsuarioDAO() {
        em = EntityManagerProvider.getEM();
    }

    public void salva(Usuario p) {
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
    }

    public void edita(Usuario p) {
        em.getTransaction().begin();
        em.merge(p);
        em.getTransaction().commit();
    }

    public void exclui(Usuario p) {
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }

    public List<Usuario> listausuario() {
        Query q = em.createQuery("select p from Usuario p order by p.login");
        List<Usuario> lista = q.getResultList();
        return lista;
    }

    public Usuario localiza(String login) {
        Query q = em.createQuery("select p from Usuario p where p.login= :login order by p.login");
        q.setParameter("login", login);
        List<Usuario> lista = q.getResultList();
        if(lista.isEmpty()==false){
            Usuario p = lista.get(0);
            return p;
        }   
        else {
            return null;

        }
    }
}
