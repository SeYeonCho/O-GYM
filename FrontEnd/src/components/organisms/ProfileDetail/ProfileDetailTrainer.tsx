import React from 'react'
import {Row, Col, List, Divider, Avatar, Image, Table, Button, Modal, message } from 'antd'
import { useEffect, useState } from 'react'
import {
  FacebookOutlined, 
  TwitterOutlined, 
  InstagramOutlined, 
  YoutubeOutlined 
  } from '@ant-design/icons'
import AWS from 'aws-sdk'
import axios from 'axios'
import styled from 'styled-components'
import { useHistory } from 'react-router'
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

const careerColumns = [
  {
    title: '회사명',
    dataIndex: 'company',
    key: 'company',
  },
  {
    title: '직책',
    dataIndex: 'role',
    key: 'role',
  },
  {
    title: '입사일자',
    dataIndex: 'startDate',
    key: 'startDate',
  },
  {
    title: '입사일자',
    dataIndex: 'endDate',
    key: 'endDate',
  },
]

const certificateColumns = [
  {
    title: '자격증명',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '발행기관',
    dataIndex: 'publisher',
    key: 'publihser',
  },
  {
    title: '취득일자',
    dataIndex: 'date',
    key: 'date',
  },
]

function ProfileDetail(props) {
  let accessToken = localStorage.getItem('accessToken');
  const { certificates, careers }  = props.userInfo
  const snsAddress = props.userInfo.snss
  const [ facebook, setFacebook ] = useState({})
  const [ instagram, setInstagram ] = useState({})
  const [ twitter, setTwitter ] = useState({})
  const [ youtube, setYoutube ] = useState({})
  const [ imageName, setImageName ] = useState('')
  const [ imageURL, setImageURL ] = useState('')
  const [logged, setLogged] = useState<string>('');
  const [role, setRole] = useState<string>('');
  const history = useHistory()
  const [ status, setStatus ] = useState(false)

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
  })

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


  return (
    <>
      <div className="containerProfile">
          <Row>
            <Col span={6} style={{display: "flex", flexDirection: "column"}}>
              {imageURL !== null ?
              <Image src={imageURL} alt="프로필"  style={{width: "90%", marginLeft: "auto", marginRight: "auto"}}/> 
              :
              <Image src={profileimagedefault} alt="프로필"  style={{width: "90%", marginLeft: "auto", marginRight: "auto"}}/> 
              }
              
              <ImageButton htmlFor="input-image">프로필 업로드</ImageButton>
              <input type="file" id="input-image" onChange={upLoad} style={{display: "none"}} />
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
                <h4 style={{fontWeight:700}}>트레이너</h4>
                <h3>{props.userInfo.nickname}</h3>
                <Divider />
                <Row>
                <Col span={8}>
                  <label style={{fontWeight:700}}>이름</label>
                </Col>
                <Col span={16}>
                  <p>{props.userInfo.username}</p>
                </Col>
                <Col span={8}>
                  <label style={{fontWeight:700}}>나이</label>
                </Col>
                <Col span={16}>
                  <p>{props.userInfo.age}</p>
                </Col>
                <Col span={8}>
                  <label style={{fontWeight:700}}>이메일</label>
                </Col>
                <Col span={16}>
                  <p>{props.userInfo.email}</p>
                </Col>
                <Col span={8}>
                  <label style={{fontWeight:700}}>연락처</label>
                </Col>
                <Col span={16}>
                  <p>{props.userInfo.tel}</p>
                </Col>
                <Col span={8}>
                  <label style={{fontWeight:700}}>성별</label>
                </Col>
                <Col span={16}>
                  <p>{props.userInfo.gender}</p>
                </Col>
                <Col span={8}>
                  <label style={{fontWeight:700}}>전공</label>
                </Col>
                <Col span={16}>
                  <p>{props.userInfo.major}</p>
                </Col>
              </Row>
                <Divider />
                <div style={{marginBottom: "1rem", fontWeight:700}}>커리어</div>
                <div>
                  <Table dataSource={careers} columns={careerColumns} pagination={false} />
                </div>
                <Divider />
                <div style={{marginBottom: "1rem", fontWeight:700}}>자격증</div>
                <Table dataSource={certificates} columns={certificateColumns} pagination={false} />
                <div>
                </div>
              </div>
              <Divider />
              <Button type="primary" onClick={showModal} danger >
                  회원탈퇴
                </Button>
                <Modal title="회원탈퇴" visible={isModalVisible} onOk={widhdrawal} onCancel={handleCancel}>
                  <p>정말 탈퇴 하시겠습니까?</p>
                </Modal>

              <Divider />
            
            </Col>
          </Row>
          <Divider />

      </div>
    </>
  )
}

export default ProfileDetail
