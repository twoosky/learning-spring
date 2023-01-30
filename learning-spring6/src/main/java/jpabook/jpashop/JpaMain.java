package jpabook.jpashop;

import jpabook.jpashop.domain.item.Book;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Book book = new Book();
            book.setName("JPA");
            book.setAuthor("sky");

            em.persist(book);

            transaction.commit();
        } catch(Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
