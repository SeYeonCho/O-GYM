import React, {useEffect} from 'react'
import {Row, Col, Divider } from 'antd'
import { Email } from '../../../recoil/atoms/Reservation/Email'
import { useRecoilState } from 'recoil'
import { ReservationState } from '../../../recoil/atoms/Reservation/ReservationState'

function TrainerInfo2(props) {
  const [ email, setEmail ] = useRecoilState(Email)
  const [ reservationTab, setReservationTab ] = useRecoilState(ReservationState)
  const teacherList = props.teacherList

  useEffect(() => {
    console.log(teacherList)
  }, [])
  return (
    <div style={{height: '100vh', display: "flex"}}>
      <div style={{height: '90vh', width: '80%', margin: "auto", overflowY: "auto"}}>
        {teacherList.map((trainer, index) => (
          <Row gutter={[0, 16]} style={{height: "30%"}}>
            <Col span={4} style={{display: "flex", flexDirection: "column"}}>
              <img src="https://imgtag.co.kr/images/210729/210729_125520/3EYZwp.jpg" alt=""  style={{width: "90%", marginLeft: "auto", marginRight: "auto"}}/>
            </Col>
            <Col span={5}>
              {trainer.username}
            </Col>
            <Col span={5}>
              {trainer.description}
            </Col>
            <Col span={5}>
              {trainer.price}
            </Col>
            <Col span={5}>
              <button onClick={() => {
                setEmail(trainer.email)
                setReservationTab(!reservationTab)
              }}>예약하기</button>
            </Col>
          </Row>
        ))}
      </div>
    </div>
  )
}

export default TrainerInfo2


// <Row>
        //   <Col span={4}>
        //     사진
        //   </Col>
        //   <Col span={5}>
        //     이름 소개
        //   </Col>
        //   <Col span={5}>
        //     자격증
        //   </Col>
        //   <Col span={5}>
        //     경력
        //   </Col>
        //   <Col span={5}>
        //     예약버튼
        //   </Col>
        // </Row>