import { Divider, message } from "antd"
import { useState } from "react";
import styled from "styled-components";
import Button from "../../atoms/Button";
import Input from "../../atoms/Input"
import Label from "../../atoms/Label";
import ButtonList from "../../molecules/ButtonList";
import ListItem from "../../molecules/ListItem"
import { useRecoilState, useRecoilValue, useResetRecoilState } from "recoil";
import axios from "axios";
import { InputState } from "../../../recoil/atoms/InputState";
// @ts-ignore
import jwt_decode from "jwt-decode";
import { useHistory } from "react-router";

const LabelDiv = styled.div`
    text-align: center;
`;

const StyledDivider = styled(Divider)`
    height: inherit;
`;

const LoginContent = (): JSX.Element => {

    const [isShow, setIsShow] = useState(true);

    const [loginInfo, setLoginInfo] = useRecoilState(InputState);
    const resetLoginInfo = useResetRecoilState(InputState);
    const history = useHistory();
    // const [LodalTokenState, setLodalTokenState] = useRecoilState(LoginTokenState);

    const handleShowButton = () => {
        setIsShow(!isShow);
    }

    const success = () => {
        message.success('성공적으로 로그인 되었습니다.');
        history.push('/');
        resetLoginInfo();
    };
    
    const error = () => {
        message.error('아이디나 비밀번호가 잘못되었습니다. 다시 입력해주세요');
        history.push('/login');
        resetLoginInfo();
    };

    const requestLogin = async () => {
        let id = loginInfo.loginEmail;
        let pw = loginInfo.loginPassword;
        console.log(id, pw);
        try {
            
            let responseData = await axios.post('https://i5b305.p.ssafy.io/api/authenticate', {
                email: id,
                password: pw
            });

            let { accessToken, refreshToken } = await responseData.data.data;

            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);

            axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
    
            let checkDate: {
                exp, email, role
            } = jwt_decode(accessToken);
            console.log(checkDate);
            let currentDate = new Date();
            let tokenDate = new Date(checkDate.exp * 1000)
            let diffTime = currentDate.getTime() - tokenDate.getTime();
            
            if (diffTime > 0) {
                let refreshData = await axios.post('/api/reissue', {
                    accessToken: accessToken,
                    refreshToken: refreshToken
                });
                let updatedTokenData = refreshData.data.data;
                axios.defaults.headers.common['Authorization'] = `Bearer ${updatedTokenData.accessToken}`;
                
                localStorage.setItem('accessToken', updatedTokenData.accessToken);
                localStorage.setItem('refreshToken', updatedTokenData.refreshToken);
            }
    
            success();
        }
        catch (err) {
            error();
        }

      
        
    }

    return (
        <>
            <LabelDiv>
                <Label label="Enter your email and password to sign in"
                    color="gray"
                    backgroundcolor=""
                />
            
            </LabelDiv>
            <Divider />
            
            <ListItem flexdirection="column">
                <Label label="Email" fontweight="bold"/>
                <Input type="text" inputType="loginEmail" placeholder="email"
                    
                />
            </ListItem>
            
            <Divider />
            
            <ListItem >
                <ListItem flexdirection="column">
                    <Label label="Password" fontweight="bold"/>
                    <Input type={isShow === true ? "password" : "text"} inputType="loginPassword" placeholder="password"/>
                </ListItem>
                
                <StyledDivider type="vertical" />
                <Button onClick={handleShowButton}
                 backgroundColor="light-gray"
                 color="black"
                    borderRadius="10px"
                >{isShow === true ? "show" : "hide"}</Button>
            
            </ListItem>

            <Divider />
            <ButtonList>
                <Button width="100%"
                    backgroundColor="light-gray"
                    color="red"
                    borderRadius="10px"
                >Forgot your password?</Button>
                <Button width="100%"
                    backgroundColor="green"
                    color="black"
                    borderRadius="10px"
                    onClick={requestLogin}
                >Log In</Button>
            </ButtonList>
            <Divider />
            <ButtonList>
                <Button width="100%" signInType="facebook">Sign in with Facebook</Button>
                <Button width="100%" signInType="google">Sign in with Google</Button>
                <Button width="100%" signInType="naver">Sign in with Naver</Button>
            </ButtonList>
        </>
    );
}

export default LoginContent;