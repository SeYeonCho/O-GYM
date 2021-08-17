import React from 'react'
import {Row, Col, List, Divider } from 'antd'
import { useEffect, useState } from 'react'
import {
  FacebookOutlined, 
  TwitterOutlined, 
  InstagramOutlined, 
  YoutubeOutlined 
  } from '@ant-design/icons'

function ProfileDetail(props) {
  const { certificates, careers }  = props.userInfo
  const snsAddress = props.userInfo.snss
  const [ facebook, setFacebook ] = useState({})
  const [ instagram, setInstagram ] = useState({})
  const [ twitter, setTwitter ] = useState({})
  const [ youtube, setYoutube ] = useState({})

const isEmpty = (param) => {
  return Object.keys(param).length === 0
}

useEffect(() => {
  for (let sns in snsAddress ) {
    if (snsAddress[sns].platform === "facebook") {
      setFacebook(snsAddress[sns])
    } else if (snsAddress[sns].platform === "instagram") {
      setInstagram(snsAddress[sns])
    } else if (snsAddress[sns].platform === "twitter") {
      setTwitter(snsAddress[sns])
    } else {
      setYoutube(snsAddress[sns])
    }
  }
})
  return (
    <>
      <div className="containerProfile">
          <Row>
            <Col span={6} style={{display: "flex", flexDirection: "column"}}>
              <img src="https://imgtag.co.kr/images/210729/210729_125520/3EYZwp.jpg" alt=""  style={{width: "90%", marginLeft: "auto", marginRight: "auto"}}/>
              <div style={{display: 'flex', justifyContent: "space-between", marginTop: "1rem", marginLeft: "0.7rem", marginRight: "0.7rem"}}>
              
              {isEmpty(facebook) ?
                <p><FacebookOutlined /></p> :
                <a
                  href={facebook["url"]}
                  target="_blank"
                  rel="noreferrer">
                  <FacebookOutlined />
                </a>}
              {isEmpty(twitter) ?
                <p><TwitterOutlined/></p> :
                <a
                  href={twitter["url"]}
                  target="_blank"
                  rel="noreferrer">
                  <TwitterOutlined/>
                </a>}
              {isEmpty(instagram) ?
                <p><InstagramOutlined/></p> :
                <a
                  href={instagram["url"]}
                  target="_blank"
                  rel="noreferrer">
                  <InstagramOutlined/>
                </a>}
              {isEmpty(youtube) ?
                <p><YoutubeOutlined/></p> :
                <a
                  href={youtube["url"]}
                  target="_blank"
                  rel="noreferrer">
                  <YoutubeOutlined/>
                </a>}
              </div>
            </Col>
            <Col span={18} style={{display: "flex", flexDirection: "column"}}>
              <div>
                <h3>{props.userInfo.username}</h3>
                <h4>피트니스 트레이너</h4>
                <Divider />
                <label>경력</label>
                  {careers.map((career, index) => (
                    <p>{career.company} {career.role} {career.startDate} ~ {career.endDate}</p>
                  ))}
                <Divider />
                <label>자격증</label>
                <div>
                  {certificates.map((certificate, index) => (
                    <p>{certificate.name} {certificate.publisher} {certificate.date}</p>
                  ))}
                </div>
              </div>

              <Divider />
              
              <Row>
                <Col span={8}>
                  <label>이름</label>
                </Col>
                <Col span={16}>
                  <p>{props.userInfo.username}</p>
                </Col>
                <Col span={8}>
                  <label>나이</label>
                </Col>
                <Col span={16}>
                  <p>{props.userInfo.age}</p>
                </Col>
                <Col span={8}>
                  <label>이메일</label>
                </Col>
                <Col span={16}>
                  <p>{props.userInfo.email}</p>
                </Col>
                <Col span={8}>
                  <label>연락처</label>
                </Col>
                <Col span={16}>
                  <p>{props.userInfo.tel}</p>
                </Col>
              </Row>
            </Col>
          </Row>
          <Divider />

      </div>
    </>
  )
}

export default ProfileDetail
