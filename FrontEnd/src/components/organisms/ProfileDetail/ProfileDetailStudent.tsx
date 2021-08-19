import React from 'react'
import {Row, Col, List, Divider, message, Modal, Button, Image } from 'antd'
import { useEffect, useState } from 'react'
import styled from 'styled-components'
import { useHistory } from 'react-router'
import axios from 'axios'
import AWS from 'aws-sdk'
import profileimagedefault from '../../../assets/pages/profile/profileimagedefault.png'

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

const StyledLabel = styled.label`
  font-size: 1rem;
  font-weight: 700;
`

const StyledP = styled.p`
font-size: 1rem;
font-weight: 500;
`;

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
      console.log(response)
      message.success('성공적으로 탈퇴 되었습니다.');
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      setLogged('');
      setRole('');
      setIsModalVisible(false);
      history.push('/')
    })
    .catch((err) => {
      message.error("회원탈퇴를 실패했습니다.")
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
        message.success("이미지 업로드에 성공했습니다.")
        window.location.reload()
      },
      function (err) {
        return message.error("이미지 업로드에 실패했습니다")
      }
    )
  }

  return (
    <>
      <div className="containerProfile" style={{marginTop: "0.8rem"}}>
          <Row>
            <Col span={5} style={{display: "flex", flexDirection: "column", marginRight: '1rem'}}>
            {imageURL !== null ?
              <Image src={imageURL} alt="프로필"  style={{width: "90%", marginLeft: "auto", marginRight: "auto"}}/> 
              :
              <Image src={profileimagedefault} alt="프로필"  style={{width: "90%", marginLeft: "auto", marginRight: "auto"}}/> 
              }
              <ImageButton htmlFor="input-image" style={{marginTop: "2rem"}}>프로필 업로드</ImageButton>
              <input type="file" id="input-image" onChange={upLoad} style={{display: "none", marginTop: "2rem"}} />
            </Col>
            <Col span={17} style={{display: "flex", flexDirection: "column", marginLeft: "1rem" }}>
              <Row style={{paddingTop: "1rem", borderTop: "1px solid black"}}>
                <Col span={8}>
                  <StyledLabel>이름</StyledLabel>
                </Col>
                <Col span={16}>
                  <StyledP>{props.userInfo.username}</StyledP>
                </Col>
                <Col span={8}>
                  <StyledLabel>나이</StyledLabel>
                </Col>
                <Col span={16}>
                  <StyledP>{props.userInfo.age}</StyledP>
                </Col>
                <Col span={8}>
                  <StyledLabel>이메일</StyledLabel>
                </Col>
                <Col span={16}>
                  <StyledP>{props.userInfo.email}</StyledP>
                </Col>
                <Col span={8}>
                  <StyledLabel>연락처</StyledLabel>
                </Col>
                <Col span={16}>
                  <StyledP>{props.userInfo.tel}</StyledP>
                </Col>
                <Col span={8}>
                  <StyledLabel>성별</StyledLabel>
                </Col>
                <Col span={16}>
                  <StyledP>{props.userInfo.gender}</StyledP>
                </Col>
              </Row>
              <Row>
                <Col span={8}>
                  <StyledLabel>키</StyledLabel>
                </Col>
                <Col span={16}>
                  {month && <StyledP>{props.userInfo.heights[month]}</StyledP>}
                </Col>
                <Col span={8}>
                  <StyledLabel>체중</StyledLabel>
                </Col>
                <Col span={16}>
                  {month && <StyledP>{props.userInfo.weights[month]}</StyledP>}
                </Col>
              </Row>
              <div style={{paddingTop: "1rem", borderTop: "1px solid black", marginTop: "2rem", marginBottom: "1rem"}}>
              <Button type="primary" onClick={showModal} danger block>
                  회원탈퇴
                </Button>
                <Modal title="회원탈퇴" visible={isModalVisible} onOk={widhdrawal} onCancel={handleCancel}>
                  <StyledP>정말 탈퇴 하시겠습니까?</StyledP>
                </Modal>
              </div> 
            </Col>
          </Row>
          <Divider />
      </div>
    </>
  )
}

export default ProfileDetailStudent
