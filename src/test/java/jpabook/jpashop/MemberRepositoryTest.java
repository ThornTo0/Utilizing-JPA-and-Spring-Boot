package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

   @Test
   @Transactional // Test 코드에 Transactional 어노테이션 사용 시 Test 후 Rollback을 진행함
   @Rollback(value = false)
   public void TestMember() throws Exception{
       //given
       Member member = new Member();
       member.setUsername("memberA");

       //when
       Long saveId = memberRepository.save(member);
       Member findMember = memberRepository.find(saveId);

       //then
       Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
       Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
       Assertions.assertThat(findMember).isEqualTo(member);

   }

}