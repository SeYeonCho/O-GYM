import React from "react";
import styled from "styled-components";
import axios from "axios";
import { useEffect } from "react";
import { useHistory } from "react-router-dom";
import { message } from 'antd'

const StyledDiv = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

function ReservationCancel(props) {
  const history = useHistory();
  const myReservationList = props.reservationList
  let accessToken = localStorage.getItem('accessToken');

  return (
    <>
      <StyledDiv style={{overflowY: "auto"}}>
      {myReservationList.map((res:any, idx:any) => (
        <div>
          <div>
            {
              res.reservationTime.substring(0, 4) + "년 " +
              res.reservationTime.substring(5, 7) + "월 " +
              res.reservationTime.substring(8, 10) + "일 " +
              res.reservationTime.substring(11, 13) + "시"
            }
          </div>
          <div style={{display: "flex"}}>
          <div>{res.nickname}</div>
          <button onClick={() => {
            console.log(res.email, res.reservationTime)
              axios({
                method: 'delete',
                url: 'https://i5b305.p.ssafy.io/api/pt/reservation',
                data: {
                  "ptTeacherEmail" : res.email,
                  "reservationTime" : res.reservationTime,
                  "description": "예약취소"
                },
                headers: {
                  "Authorization": `Bearer ${accessToken}`
                }
              })
              .then((response) => {
                message.success('예약이 취소되었습니다.')
                history.push('/profile')
              })
          }}>예약취소</button>
          </div>
        </div>
      ))}
        
      </StyledDiv>
    </>
  );
}

export default ReservationCancel;
