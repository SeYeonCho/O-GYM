import react, { useState } from "react";
import ReactDOM from "react-dom";
import "antd/dist/antd.css";
import { Input, Space, Radio } from "antd";
import styled from "styled-components";

const StyledDiv = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const { Search } = Input;

const onSearch = (value) => console.log(value);

function TrainerSearch() {
  const [gender, setgender] = useState(1);

  const onChange = (e) => {
    console.log("radio checked", e.target.value);
    setgender(e.target.value);
  };

  return (
    <StyledDiv>
      {/* <Space direction="vertical" style={{ marginTop: "1.5rem" }}> */}
      <label>트레이너 검색</label>
      <Search
        placeholder="트레이너 검색"
        allowClear
        onSearch={onSearch}
        enterButton
        size="large"
      />
      <label>성별</label>
      <Radio.Group onChange={onChange} value={gender}>
        <Space direction="vertical">
          <Radio value={1}>남성</Radio>
          <Radio value={2}>여성</Radio>
        </Space>
      </Radio.Group>
      <label>기타 조건 검색 1</label>
      <Radio.Group>
        <Space direction="vertical">
          <Radio value={1}>radio 1</Radio>
          <Radio value={2}>radio 2</Radio>
        </Space>
      </Radio.Group>
      <label>기타 조건 검색 2</label>
      <Radio.Group>
        <Space direction="vertical">
          <Radio value={1}>radio 1</Radio>
          <Radio value={2}>radio 2</Radio>
        </Space>
      </Radio.Group>
      {/* </Space> */}
    </StyledDiv>
  );
}

export default TrainerSearch;
