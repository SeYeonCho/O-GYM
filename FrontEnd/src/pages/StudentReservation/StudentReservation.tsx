import React, {useState, useEffect} from 'react'
import styled from 'styled-components'
import 'antd/dist/antd.css';
import { Layout, Menu } from 'antd';
import { Row, Col, Button, message } from 'antd';
import TrainerSearch from '../../components/organisms/TrainerSearch/TrainerSearch';
import axios from 'axios';
import './styles.css';
import {
  FieldTimeOutlined,
  BarChartOutlined,
  CloudOutlined,
  ShopOutlined,
  TeamOutlined,
  UserOutlined,
  UploadOutlined,
  VideoCameraOutlined, 
  CalendarOutlined, 
  SettingOutlined
} from '@ant-design/icons';
import StudentCalendar from '../../components/molecules/StudentCalendar';
import TimeSchedule from '../../components/molecules/TimeSchedule';
import MainNavigation from '../../components/organisms/Main/Main-Navigation';
import TrainerInfo2 from '../../components/organisms/TrainerInfo/TrainerInfo2';
import { ReservationState } from '../../recoil/atoms/Reservation/ReservationState';
import { useRecoilState } from 'recoil';
import { Email } from '../../recoil/atoms/Reservation/Email';
import { Time } from '../../recoil/atoms/Reservation/Time';
import { Date } from '../../recoil/atoms/Reservation/Date';
import { ReservationList } from '../../recoil/atoms/Reservation/ReservationList';
import ReservationCancel from '../../components/organisms/ReservationCancel/ReservationCancel';
import { useHistory } from 'react-router-dom';

const { Content, Sider } = Layout;
const { SubMenu } = Menu;

const Container = styled(Row)`
  height: 100vh;
  display: flex;
`;


const StyledSider = styled(Sider)`
  overflow: auto;
  height: 100vh;
  background: none;
  max-width: none;
  min-width: none;
  width: auto;
  /* position: fixed; */
  /* right: 0; */
  /* width: 100%; */
`

const StyledDiv = styled.div`
  height: 50vh;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0;
  padding: 0;
  flex-direction: column;
`;

function StudentReservation() {
  const history = useHistory();
  let accessToken = localStorage.getItem('accessToken');
  // const [selectReservation, setSelectReservation] = useState(false);
  const [teacherList, setTeacherList] = useState<any>([])
  const [reservationTab, setReservationTab] = useRecoilState(ReservationState)
  const [email, setEmail] = useRecoilState(Email)
  const [time, setTime] = useRecoilState(Time)
  const [date, setDate] = useRecoilState(Date)
  const [reservationList, setReservationList] = useRecoilState(ReservationList)

  const handleClick = (e: any) => {
    console.log('click ', e);
  };

  // const onClick = (e) => {
  //   setSelectReservation(!selectReservation)
  //   console.log(e)
  // }

  function ptReservation () {

    console.log(date, time, email)
    axios({
      method: 'post',
      url: 'https://i5b305.p.ssafy.io/api/pt/reservation',
      data: {
        ptTeacherEmail : email,
        reservationTime : date+"T"+time+":00",
        description : "예약"
      },
      headers: {
        "Authorization": `Bearer ${accessToken}`
      }
    })
    .then((response) => {
      message.success('성공적으로 예약 되었습니다.');
      history.push('/profile')
    })
  }

  useEffect(() => {
    let teacher = []
    axios.get(
      'https://i5b305.p.ssafy.io/api/pt/teacherlist', {
        headers: {
          "Authorization": `Bearer ${accessToken}`
        }
      }
    )
    .then((response) => {
      console.log(response.data.data.teacherList)
      setTeacherList(response.data.data.teacherList)
    })

    axios.get('https://i5b305.p.ssafy.io/api/pt/reservation', {
      headers: {
        "Authorization": `Bearer ${accessToken}`
      }
    })
    .then((response) => {
      setReservationList(response.data.data)
      console.log(response.data.data)
    })
  }, [])


  return (
    <Layout>
      <MainNavigation />
      <Content>
          <TrainerInfo2 teacherList={teacherList}/>
      </Content>
      <Sider style={{height: '100vh', background: "none"}}>
        { reservationTab ?
          <StyledSider><div className="logo" />
          <Menu
            onClick={handleClick}
            style={{ width: "auto" }}
            defaultSelectedKeys={['1']}
            defaultOpenKeys={['sub1']}
            mode="inline" 
          >
          <SubMenu key="sub1" icon={<CalendarOutlined />} title="날짜선택">
            <StudentCalendar />
          </SubMenu>
          <SubMenu key="sub2" icon={<FieldTimeOutlined />} title="시간선택">
            <TimeSchedule />
          </SubMenu>
            <div>
              <p>날짜: </p>
              <p>시간: </p>
              <p>금액: </p>
            </div>
            <Button type="primary" >예약닫기</Button>
            <Button type="primary" onClick={ptReservation}>예약하기</Button>
          </Menu></StyledSider>
          :
          <>
            <StyledDiv>
              <TrainerSearch />
            </StyledDiv>
            <StyledDiv>
              <ReservationCancel reservationList={reservationList} />
            </StyledDiv>
          </>
          }
      </Sider>
    </Layout>
    // <Container align='middle' justify='center' >
    //   <MainNavigation />
    //   <Col span={18}>
    //     <div style={{margin: 'auto'}}>
    //       <TrainerInfo onClick={onClick}/>
    //     </div>
    //   </Col>
    //   <Col span={6}>
    //     { selectReservation ?
    //     <StyledSider><div className="logo" />
    //     <Menu
    //     onClick={handleClick}
    //     style={{ width: "auto" }}
    //     defaultSelectedKeys={['1']}
    //     defaultOpenKeys={['sub1']}
    //     mode="inline"
        
    //   >
    //     <SubMenu key="sub1" icon={<CalendarOutlined />} title="날짜선택">
    //       <StudentCalendar />
    //     </SubMenu>
    //     <SubMenu key="sub2" icon={<FieldTimeOutlined />} title="시간선택">
    //       <TimeSchedule />
    //     </SubMenu>
    //     <Button type="primary" onClick={onClick}>예약하기</Button>
    //   </Menu></StyledSider>
    //   :
    //   <>
    //   <StyledDiv>
    //   <TrainerSearch />
    //   </StyledDiv>
    //   <StyledDiv>
    //     <Payment />
    //   </StyledDiv>
    //   </>
    //   }


    //   </Col>
    // </Container>
  )
}

export default StudentReservation