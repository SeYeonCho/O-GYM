import React from "react";
import ReactDOM from "react-dom";
import "antd/dist/antd.css";
import "./styles.css";
import styled from "styled-components";
import { List, message, Avatar, Spin, Image, Row, Col, Space } from "antd";
import reqwest from "reqwest";
import {
  FacebookOutlined,
  InstagramOutlined,
  TwitterOutlined,
} from "@ant-design/icons";

import InfiniteScroll from "react-infinite-scroller";

const StyledList = styled(List)`
  .ant-list-item-meta {
    margin-bottom: 0;
  }

  .ant-list-item-extra {
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .ant-list-item-action {
    margin-left: 1rem;
    margin-top: 0;
  }
`;

// const StyledListItemMeta = styled(List.Item.Meta)`
//   .ant-list-item-meta {
//     margin-bottom: 0;
//   }
// `;

const fakeDataUrl =
  "https://randomuser.me/api/?results=5&inc=name,gender,email,nat&noinfo";

const IconText = ({ icon, text }) => (
  <Space>
    {React.createElement(icon)}
    {text}
  </Space>
);

class TrainerInfo extends React.Component {
  state = {
    data: [],
    loading: false,
    hasMore: true,
  };

  componentDidMount() {
    this.fetchData((res) => {
      this.setState({
        data: res.results,
      });
    });
  }

  fetchData = (callback) => {
    reqwest({
      url: fakeDataUrl,
      type: "json",
      method: "get",
      contentType: "application/json",
      success: (res) => {
        callback(res);
      },
    });
  };

  handleInfiniteOnLoad = () => {
    let { data } = this.state;
    this.setState({
      loading: true,
    });
    if (data.length > 14) {
      message.warning("트레이너를 모두 불러왔습니다.");
      this.setState({
        hasMore: false,
        loading: false,
      });
      return;
    }
    this.fetchData((res) => {
      data = data.concat(res.results);
      this.setState({
        data,
        loading: false,
      });
    });
  };

  render() {
    return (
      <div className="demo-infinite-container">
        <InfiniteScroll
          initialLoad={false}
          pageStart={0}
          loadMore={this.handleInfiniteOnLoad}
          hasMore={!this.state.loading && this.state.hasMore}
          useWindow={false}
        >
          <StyledList
            itemLayout="vertical"
            dataSource={this.state.data}
            renderItem={(item) => (
              <List.Item
                key={item.id}
                actions={[
                  <a
                    href="https://www.facebook.com/"
                    target="_blank"
                    rel="noreferrer"
                  >
                    <IconText
                      icon={FacebookOutlined}
                      text=""
                      key="list-vertical-star-o"
                    />
                  </a>,
                  <a
                    href="https://www.instagram.com/"
                    target="_blank"
                    rel="noreferrer"
                  >
                    <IconText
                      icon={InstagramOutlined}
                      text=""
                      key="list-vertical-like-o"
                    />
                  </a>,
                  <a
                    href="https://twitter.com/?lang=en"
                    target="_blank"
                    rel="noreferrer"
                  >
                    <IconText
                      icon={TwitterOutlined}
                      text=""
                      key="list-vertical-message"
                    />
                  </a>,
                ]}
                extra={<button onClick={this.props.onClick}>예약하기</button>}
              >
                <List.Item.Meta
                  avatar={
                    <Image
                      width={150}
                      src="https://imgtag.co.kr/images/210729/210729_125520/3EYZwp.jpg"
                    />
                  }
                  title={
                    <Row>
                      <Col span={12}>
                        <p>#남성 #재활전문 #기타 등등</p>
                        <p>나이: 27세</p>
                        <p>신장: 183cm</p>
                        <p>가격: 5만원 (1시간)</p>
                        <p>평점: </p>
                      </Col>
                      <Col span={12}>
                        <p>저는 이러이러한 사람이고</p>
                        <p>저는 이러이러한 사람이고</p>
                        <p>저는 이러이러한 사람이고</p>
                        <p>저는 이러이러한 사람이고</p>
                      </Col>
                      <button onClick={() => console.log(List.Item)}>
                        버튼
                      </button>
                    </Row>
                  }
                  // description={item.email}
                />
                {/* <button>예약하기</button> */}
              </List.Item>
            )}
          >
            {this.state.loading && this.state.hasMore && (
              <div className="demo-loading-container">
                <Spin />
              </div>
            )}
          </StyledList>
        </InfiniteScroll>
      </div>
    );
  }
}

export default TrainerInfo;
