import React from "react";
import styled from "styled-components";

const StyledDiv = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

function Payment() {
  return (
    <>
      <StyledDiv>
        <p>시간당 금액 75,000원</p>
        <p>총 할인금액 25,000원</p>
        <p>총 결제금액 50,000원</p>
        <button>주문하기</button>
      </StyledDiv>
      <StyledDiv>
        <div>이용안내 +</div>
        <div>주의사항 +</div>
      </StyledDiv>
    </>
  );
}

export default Payment;
