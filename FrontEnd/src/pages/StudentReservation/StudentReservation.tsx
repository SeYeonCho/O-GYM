import React, {useState} from 'react'
import styled from 'styled-components'
import 'antd/dist/antd.css';
import { Layout, Menu } from 'antd';
import { Row, Col, Button } from 'antd';
import TrainerInfo from '../../components/organisms/TrainerInfo/TrainerInfo';
import TrainerSearch from '../../components/organisms/TrainerSearch/TrainerSearch';
import Payment from '../../components/organisms/Payment/Payment';
import './styles.css';
import {
  AppstoreOutlined,
  BarChartOutlined,
  CloudOutlined,
  ShopOutlined,
  TeamOutlined,
  UserOutlined,
  UploadOutlined,
  VideoCameraOutlined, 
  MailOutlined, 
  SettingOutlined
} from '@ant-design/icons';
import StudentCalendar from '../../components/molecules/StudentCalendar';
import TimeSchedule from '../../components/molecules/TimeSchedule';
import MainNavigation from '../../components/organisms/Main/Main-Navigation';

const { Content, Sider } = Layout;
const { SubMenu } = Menu;

const Container = styled(Row)`
  height: 100vh;
  display: flex;
`;


const StyledSider = styled(Sider)`
  overflow: auto;
  height: 100vh;
  width: 100vw;
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

  const [selectReservation, setSelectReservation] = useState(false);

  const handleClick = (e: any) => {
    console.log('click ', e);
  };

  const onClick = () => {
    setSelectReservation(!selectReservation)
  }

  return (
    <Container align='middle' justify='center' >
      <MainNavigation />
      <Col span={18}>
        <div style={{margin: 'auto'}}>
          <TrainerInfo onClick={onClick}/>
        </div>
      </Col>
      <Col span={6}>
        { selectReservation ?
        <StyledSider><div className="logo" />
        <Menu
        onClick={handleClick}
        style={{ width: 256 }}
        defaultSelectedKeys={['1']}
        defaultOpenKeys={['sub1']}
        mode="inline"
      >
        <SubMenu key="sub1" icon={<MailOutlined />} title="날짜선택">
          <Menu.ItemGroup key="g1" title="날짜선택">
          <StudentCalendar />
          </Menu.ItemGroup>
        </SubMenu>
        <SubMenu key="sub2" icon={<AppstoreOutlined />} title="시간선택">
          <TimeSchedule />
        </SubMenu>
        <Button type="primary" onClick={onClick}>예약하기</Button>
      </Menu></StyledSider>
      :
      <>
      <StyledDiv>
      <TrainerSearch />
      </StyledDiv>
      <StyledDiv>
        <Payment />
      </StyledDiv>
      </>
      }


      </Col>
    </Container>
  )
}

export default StudentReservation
