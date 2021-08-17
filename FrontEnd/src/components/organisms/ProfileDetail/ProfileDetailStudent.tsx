import React from 'react'
import {Row, Col, List, Divider } from 'antd'
import { useEffect, useState } from 'react'
import {
  FacebookOutlined, 
  TwitterOutlined, 
  InstagramOutlined, 
  YoutubeOutlined 
  } from '@ant-design/icons'

function ProfileDetailStudent(props) {
  const date = new Date()
  const month = date.getMonth()


  useEffect(() => {
  })
  return (
    <>
      <div className="containerProfile">
          <Row>
            <Col span={6} style={{display: "flex", flexDirection: "column"}}>
              <img src="https://imgtag.co.kr/images/210729/210729_125520/3EYZwp.jpg" alt=""  style={{width: "90%", marginLeft: "auto", marginRight: "auto"}}/>
            </Col>
            <Col span={18} style={{display: "flex", flexDirection: "column"}}>
              <div>
                <h3>{props.userInfo.username}</h3>
                <h4>학생</h4>
              </div>
              <Divider />
              <Row>
                <Col span={8}>
                  <label>키</label>
                </Col>
                <Col span={16}>
                  <p>{props.userInfo.heights[month]}</p>
                </Col>
                <Col span={8}>
                  <label>체중</label>
                </Col>
                <Col span={16}>
                  <p>{props.userInfo.weights[month]}</p>
                </Col>
              </Row>
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

export default ProfileDetailStudent
