import { Modal, Tabs } from "antd";
import { useRecoilState } from "recoil";
import styled from "styled-components";
import LoginContent from "../../components/organisms/Login/Login-content";
import { ModalState } from "../../recoil/pages/LoginPageState";
import RegisterStudent from "../../components/organisms/Register/Register-Student";
import RegisterTrainer from "../../components/organisms/Register/Register-Trainer";

const { TabPane } = Tabs;

// const LoginTag = () => {
//     return (
//         <div className={styles.loginwidth}>

//         </div>
//     );
// }


const StyledTabs = styled(Tabs)`
    width: 100%;

    .ant-tabs-tab, .ant-tabs-nav-list {
        width: 100%;
    }  
`;

const StyledTabPane = styled(TabPane)`
    width: 100%;
`;

const LoginPage = (): JSX.Element => {
    
    // const [isModalVisible, setIsModalVisible] = useRecoilState(ModalState);

    // const handleOk = () => {
    //     setIsModalVisible(false);
    // }

    // const handleCancel = () => {
    //     setIsModalVisible(false);
    // }

    return (
        <>
            <Modal title={null}
                closable={false}
                visible={true}
                footer={null}
                // onOk={handleOk}
                // onCancel={handleCancel}
            >
                <StyledTabs  type="card">
                    <StyledTabPane tab="LOGIN" key="1">
                        <LoginContent />
                    </StyledTabPane>
                    
                    <StyledTabPane tab="REGISTER" key="2">
                        <StyledTabs type="card">
                          <StyledTabPane tab="TRAINEE" key="3">
                            <RegisterStudent />
                          </StyledTabPane>
                          <StyledTabPane tab="TRAINER" key="4">
                            <RegisterTrainer />
                          </StyledTabPane>
                        </StyledTabs>
                    </StyledTabPane>

                </StyledTabs>
            </Modal>
        </>
    );
}

export default LoginPage;