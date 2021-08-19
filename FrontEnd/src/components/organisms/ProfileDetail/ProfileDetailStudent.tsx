import React from 'react'
import {Row, Col, List, Divider, message, Modal, Button, Image } from 'antd'
import { useEffect, useState } from 'react'
import styled from 'styled-components'
import { useHistory } from 'react-router'
import axios from 'axios'
import AWS from 'aws-sdk'

const ImageButton = styled.label`
  text-align: center;
  padding: 6px 20px;
  background-color:#FF6600;
  margin-left: 0.7rem;
  margin-right: 0.7rem;
  border-radius: 4px;
  color: white;
  cursor: pointer;
`

function ProfileDetailStudent(props) {
  let accessToken = localStorage.getItem('accessToken');
  const history = useHistory()
  const date = new Date()
  const month = date.getMonth()
  const [ imageName, setImageName ] = useState('')
  const [ imageURL, setImageURL ] = useState('')
  const [logged, setLogged] = useState<string>('');
  const [role, setRole] = useState<string>('');

  const [isModalVisible, setIsModalVisible] = useState(false);

  const showModal = () => {
    setIsModalVisible(true);
  };

  const handleOk = () => {
    setIsModalVisible(false);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };

  useEffect(() => {
    axios.get(`${process.env.REACT_APP_API_ROOT_ADDRESS}/api/user/email, profilePictureURL`, {
      headers: {
        "Authorization": `Bearer ${accessToken}`
      }
    })
    .then((response) => {
      console.log(response.data.data);
      setImageName(response.data.data.email);
      setImageURL(response.data.data.profilePictureURL)
    })
  }, [])


  function widhdrawal () {

    axios({
      method: 'delete',
      url: `${process.env.REACT_APP_API_ROOT_ADDRESS}/api/user`,
      headers: {
        "Authorization": `Bearer ${accessToken}`
      }
    })
    .then((response) => {
      message.success('성공적으로 탈퇴 되었습니다.');
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      setLogged('');
      setRole('');
      setIsModalVisible(false);
      history.push('/')
    })
  }

  AWS.config.update({
    region: "ap-northeast-2",
    accessKeyId: `${process.env.REACT_APP_S3_ACCESS_KEY_ID}`, // accessKeyId
    secretAccessKey: `${process.env.REACT_APP_S3_SECRET_ACCESS_KEY}`, // secretAccessKey
    
  })

  function upLoad (e:any) {
    const file = e.target.files[0]

    // S3 SDK에 내장된 업로드 함수
    const upload = new AWS.S3.ManagedUpload({
      params: {
        Bucket: "ogymbucket",
        Key: `profiles/${imageName}`, // 업로드할 파일명 (* 확장자를 추가해야 합니다!)
        ContentType: e.target.files[0].type,
        Body: file, // 업로드할 파일 객체
      },
    })
  
    const promise = upload.promise()
  
    promise.then(
      function (data) {
        axios({
          method: 'patch',
          url: `${process.env.REACT_APP_API_ROOT_ADDRESS}/api/user`,
          data: {
            "url" : data.Location
          },
          headers: {
            "Authorization": `Bearer ${accessToken}`
          }
        })
        alert("이미지 업로드에 성공했습니다.")
        window.location.reload()
      },
      function (err) {
        return alert("오류가 발생했습니다: ")
      }
    )
  }

  return (
    <>
      <div className="containerProfile">
          <Row>
            <Col span={6} style={{display: "flex", flexDirection: "column"}}>
            <Image src={imageURL} alt=""  style={{width: "90%", marginLeft: "auto", marginRight: "auto"}}/>
              <ImageButton htmlFor="input-image">프로필 업로드</ImageButton>
              <input type="file" id="input-image" onChange={upLoad} style={{display: "none"}} />
            </Col>
            <Col span={18} style={{display: "flex", flexDirection: "column"}}>
              <div>
                <h3>{props.userInfo.nickname}</h3>
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
                <Col span={8}>
                  <label>성별</label>
                </Col>
                <Col span={16}>
                  <p>{props.userInfo.gender}</p>
                </Col>
              </Row>
              <Row>
                <Col span={8}>
                  <label>키</label>
                </Col>
                <Col span={16}>
                  {month && <p>{props.userInfo.heights[month]}</p>}
                </Col>
                <Col span={8}>
                  <label>체중</label>
                </Col>
                <Col span={16}>
                  {month && <p>{props.userInfo.weights[month]}</p>}
                </Col>
              </Row>
              <Divider />
              <Button type="primary" onClick={showModal} danger >
                  회원탈퇴
                </Button>
                <Modal title="회원탈퇴" visible={isModalVisible} onOk={widhdrawal} onCancel={handleCancel}>
                  <p>정말 탈퇴 하시겠습니까?</p>
                </Modal>

            </Col>
          </Row>
          <Divider />

      </div>
    </>
  )
}

export default ProfileDetailStudent
