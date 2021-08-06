import React from 'react'
import styled from 'styled-components'
import 'antd/dist/antd.css';
import { Layout, Menu } from 'antd';
import { Row, Col } from 'antd';
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

const { Content, Sider } = Layout;
const { SubMenu } = Menu;

const Container = styled(Row)`
  height: 100vh;
  display: flex;
`;

const StyledSider = styled(Sider)`
  overflow: auto;
  height: 100vh;
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

  const handleClick = (e: any) => {
    console.log('click ', e);
  };

  return (
    <Container align='middle' justify='center' >
      <Col span={18}>
        <div style={{margin: 'auto'}}>
          <TrainerInfo />
        </div>
      </Col>
      <Col span={6}>
        {/* <StyledSider><div className="logo" />
        <Menu
        onClick={handleClick}
        style={{ width: 256 }}
        defaultSelectedKeys={['1']}
        defaultOpenKeys={['sub1']}
        mode="inline"
      >
        <SubMenu key="sub1" icon={<MailOutlined />} title="Navigation One">
          <Menu.ItemGroup key="g1" title="Item 1">
            <Menu.Item key="1">Option 1</Menu.Item>
            <Menu.Item key="2">Option 2</Menu.Item>
          </Menu.ItemGroup>
          <Menu.ItemGroup key="g2" title="Item 2">
            <Menu.Item key="3">Option 3</Menu.Item>
            <Menu.Item key="4">Option 4</Menu.Item>
          </Menu.ItemGroup>
        </SubMenu>
        <SubMenu key="sub2" icon={<AppstoreOutlined />} title="Navigation Two">
          <Menu.Item key="5">Option 5</Menu.Item>
          <Menu.Item key="6">Option 6</Menu.Item>
          <SubMenu key="sub3" title="Submenu">
            <Menu.Item key="7">Option 7</Menu.Item>
            <Menu.Item key="8">Option 8</Menu.Item>
          </SubMenu>
        </SubMenu>
        <SubMenu key="sub4" icon={<SettingOutlined />} title="Navigation Three">
          <Menu.Item key="9">Option 9</Menu.Item>
          <Menu.Item key="10">Option 10</Menu.Item>
          <Menu.Item key="11">Option 11</Menu.Item>
          <Menu.Item key="12">Option 12</Menu.Item>
        </SubMenu>
      </Menu></StyledSider> */}


        <StyledDiv>
          <TrainerSearch />
        </StyledDiv>
        <StyledDiv>
          <Payment />
        </StyledDiv>
      </Col>
    </Container>
  )
}

export default StudentReservation
