import { Modal, Button } from "antd";
import { useState } from "react";
import styled from "styled-components";
import { Link } from "react-router-dom";
import { NavigationData } from "./Main-NavigationData";

const StyledModal = styled(Modal)`
    
     .ant-modal-content {
            height: 100%;
    }

`;

const StyledLi = styled.li`
  {
    display: flex;
    justify-content: start;
    align-items: center;
    padding: 8px 0px 8px 16px;
    list-style: none;
    height: 60px;
  }
  a {
    text-decoration: none;
    color: black;
    font-size: 18px;
    height: 100%;
    display: flex;
    align-items: center;
    padding: 0 16px;
    border-radius: 4px;
  }
  a:hover {
    background-color: #1a83ff;
  }


`;

const MainNavigation = (): JSX.Element => {

    const [isVisible, setIsVisible] = useState<boolean>(false);
    
    const clickMenuButton = () => {
        setIsVisible(!isVisible)
    }

    const clickModalClose = () => {
        setIsVisible(!isVisible)
    }

    return (
        <>
            <Button onClick={clickMenuButton}>
                show
            </Button>
            <StyledModal
                title="O-GYM"
                style={isVisible ? 
                  {
                    position: "fixed",
                    top: "0px",
                    left: "0px",
                    bottom: "0px",
                    height: "100%",
                    animation: "none",
                    transition: "350ms",
                  } :
                  {
                    position: "fixed",
                    top: "0px",
                    left: "-150px",
                    bottom: "0px",
                    height: "100%",
                    animation: "none",
                    transition: "550",
                  }
              }
                visible={isVisible}
                onOk={clickModalClose}
                onCancel={clickModalClose}
                footer={null}
            >
              <ul style={{width: "100%"}}>
                {NavigationData.map((item, index) => {
                  return (
                    <StyledLi key={index} className={item.cName}>
                      <Link to={item.path}>
                        {item.icon}
                        <span style={{marginLeft: "16px"}}>{item.title}</span>
                      </Link>
                    </StyledLi>
                  );
                })}
              </ul>
             

            </StyledModal>
            
        </>
    );
}

export default MainNavigation;
