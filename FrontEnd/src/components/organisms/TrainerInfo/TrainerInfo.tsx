import React, { useState, useEffect } from "react";
import ReactDOM from "react-dom";
import "antd/dist/antd.css";
import "./styles.css";
import styled from "styled-components";
import { List, message, Avatar, Spin, Image, Row, Col, Space, Button, Divider } from "antd";
import reqwest from "reqwest";
import {
  FacebookOutlined as facebook,
  InstagramOutlined as instagram,
  TwitterOutlined as twitter,
  YoutubeOutlined as youtube,
} from "@ant-design/icons";

import InfiniteScroll from "react-infinite-scroller";
import axios from "axios";
import Item from "antd/lib/list/Item";
import { Email } from '../../../recoil/atoms/Reservation/Email'
import { useRecoilState } from 'recoil'
import { ReservationState } from '../../../recoil/atoms/Reservation/ReservationState'

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

const IconText = ({ icon, text }) => (
  <Space>
    {React.createElement(icon)}
    {text}
  </Space>
);

function TrainerInfo () {
  let accessToken = localStorage.getItem('accessToken');
  const [ data, setData ] = useState<any>([])
  const [ loading, setLoading ] = useState(false)
  const [ hasMore, setHasMore ] = useState(true)
  const [ num, setNum ] = useState(0)
  const [ email, setEmail ] = useRecoilState(Email)
  const [ reservationTab, setReservationTab ] = useRecoilState(ReservationState)


  const fetchData = (callback) => {
    axios.get(`https://i5b305.p.ssafy.io/api/pt/teacherlist?page=${num}&size=5`, {
      headers: {
        "Authorization": `Bearer ${accessToken}`
      }
    })
    .then((res) => {
      callback(res)
      setNum(num+1)
    })
  }
  
  useEffect(() => {
    fetchData((res) => {
      setData(res.data.data.teacherList)
      console.log(res.data.data.teacherList[0])
    })
  }, [])


  const handleInfiniteOnLoad = () => {
    let trainerData = data
    setLoading(true)
    if (data.length > 500) {
      message.warning("트레이너를 모두 불러왔습니다.");
      setHasMore(false)
      setLoading(false)
      return;
    }
    fetchData((res) => {
      trainerData = trainerData.concat(res.data.data.teacherList);
      setData(trainerData)
      setLoading(false)
    });
  };

  return (
    <div className="demo-infinite-container" style={{height: "70vh"}}>
      <InfiniteScroll
        initialLoad={false}
        pageStart={0}
        loadMore={handleInfiniteOnLoad}
        hasMore={!loading && hasMore}
        useWindow={false}
      >
        <StyledList
          itemLayout="vertical"
          dataSource={data}
          renderItem={(item:any) => (
            <List.Item
              key={item.id}
              
              extra={<Button type="primary" size="large" onClick={() => {
                setEmail(item.email)
                setReservationTab(!reservationTab)
              }}>예약하기</Button>}
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
                    <Col span={8}>
                      <label>소개</label>
                      <Divider />
                      <p>닉네임: {item.nickname}</p>
                      <p>성별: {item.gender} </p>
                      <p>나이: {item.age}</p>
                      <p>전공: {item.major}</p>
                      {item.snsList[0] && <a style={{marginRight: "1rem"}}
                  href={item.snsList[0]['url']}
                  target="_blank"
                  rel="noreferrer"
                >
                  {item.snsList[0]['platform'] === 'facebook' && <IconText
                    icon={facebook}
                    text=""
                    key="list-vertical-star-o"
                  />}
                  {item.snsList[0]['platform'] === 'instagram' && <IconText
                    icon={instagram}
                    text=""
                    key="list-vertical-star-o"
                  />}
                  {item.snsList[0]['platform'] === 'twitter' && <IconText
                    icon={twitter}
                    text=""
                    key="list-vertical-star-o"
                  />}
                  {item.snsList[0]['platform'] === 'youtube' && <IconText
                    icon={youtube}
                    text=""
                    key="list-vertical-star-o"
                  />}
                </a>}
                {item.snsList[1] && <a
                style={{marginRight: "1rem"}} 
                href={item.snsList[1]['url']}
                target="_blank"
                rel="noreferrer"
              >
                {item.snsList[1]['platform'] === 'facebook' && <IconText
                  icon={facebook}
                  text=""
                  key="list-vertical-star-o"
                />}
                {item.snsList[1]['platform'] === 'instagram' && <IconText
                  icon={instagram}
                  text=""
                  key="list-vertical-star-o"
                />}
                {item.snsList[1]['platform'] === 'twitter' && <IconText
                  icon={twitter}
                  text=""
                  key="list-vertical-star-o"
                />}
                {item.snsList[1]['platform'] === 'youtube' && <IconText
                  icon={youtube}
                  text=""
                  key="list-vertical-star-o"
                />}
              </a>}
                {item.snsList[2] && <a
                style={{marginRight: "1rem"}}
                href={item.snsList[2]['url']}
                target="_blank"
                rel="noreferrer"
              >
                {item.snsList[2]['platform'] === 'facebook' && <IconText
                  icon={facebook}
                  text=""
                  key="list-vertical-star-o"
                />}
                {item.snsList[2]['platform'] === 'instagram' && <IconText
                  icon={instagram}
                  text=""
                  key="list-vertical-star-o"
                />}
                {item.snsList[2]['platform'] === 'twitter' && <IconText
                  icon={twitter}
                  text=""
                  key="list-vertical-star-o"
                />}
                {item.snsList[2]['platform'] === 'youtube' && <IconText
                  icon={youtube}
                  text=""
                  key="list-vertical-star-o"
                />}
              </a>}
                {item.snsList[3] && <a
                style={{marginRight: "1rem"}}
                href={item.snsList[3]['url']}
                target="_blank"
                rel="noreferrer"
              >
                {item.snsList[3]['platform'] === 'facebook' && <IconText
                  icon={facebook}
                  text=""
                  key="list-vertical-star-o"
                />}
                {item.snsList[3]['platform'] === 'instagram' && <IconText
                  icon={instagram}
                  text=""
                  key="list-vertical-star-o"
                />}
                {item.snsList[3]['platform'] === 'twitter' && <IconText
                  icon={twitter}
                  text=""
                  key="list-vertical-star-o"
                />}
                {item.snsList[3]['platform'] === 'youtube' && <IconText
                  icon={youtube}
                  text=""
                  key="list-vertical-star-o"
                />}
              </a>}
                    </Col>
                    <Col span={8}>
                      <label>경력</label>
                      <Divider />
                      {item.careers.map((career:any, index:any) => (
                        <p>{career.company} / {career.role}</p>
                      ))}
                    </Col>
                    <Col span={8}>
                      <label>자격증</label>
                      <Divider />
                      {item.certificates.map((certificate:any, index:any) => (
                        <p>{certificate.name} / {certificate.publisher}</p>
                      ))}
                    </Col>
                  </Row>
                }
                description={item.description + ' / ' + item.tel + ' / ' + item.email} 
              />
            </List.Item>
          )}
        >
          {loading && hasMore && (
            <div className="demo-loading-container">
              <Spin />
            </div>
          )}
        </StyledList>
      </InfiniteScroll>
    </div>
  );
}

export default TrainerInfo;
