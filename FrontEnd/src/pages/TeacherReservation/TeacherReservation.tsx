import styled from "styled-components";
import TeacherCalender from "./TeacherCalender/TeacherCalender";
import TeacherReservationConfirm from "./TeacherReservationConfirm/TeacherReservationConfirm"
import '@fontsource/roboto';
import MainNavigation from "../../components/organisms/Main/Main-Navigation";
import TeacherTodoList from "./TeacherTodoList";
import axios from "axios";
const StyledDiv = styled.div`
   display: flex;
`;
const StyledColumnDiv = styled.div`
   display: flex;
   flex-direction: column;
`;


const TeacherReservation = (): JSX.Element => {
    return (
        <>
            <StyledDiv>
                <StyledColumnDiv
                    >
                    <div style={{height: "10vh"}}>
                        <MainNavigation position="sticky" /> 
                    </div>
                    <TeacherReservationConfirm />
                </StyledColumnDiv>
                <StyledColumnDiv>
                    <TeacherCalender />
                    <TeacherTodoList />
                </StyledColumnDiv>
            </StyledDiv>
        </>
    );
}

export default TeacherReservation;