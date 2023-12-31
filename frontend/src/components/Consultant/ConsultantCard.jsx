import { useEffect, useState } from "react"
import styles from "./ConsultantCard.module.css"
import { useNavigate } from "react-router-dom"
import { useDispatch } from "react-redux"
import { changeCsltInfo } from "../../store/consultantInfo"
import axios from "axios"
import { changeCsltTimes } from "../../store/consultantTimes"


function ConsultantCard({ consultant }) {
  const dispatch = useDispatch()

  const [imgsrc, setImgsrc] = useState("/assets/user.png")
  const navigate = useNavigate()

  const goRev = async () => {
    await dispatch(changeCsltInfo(consultant))
    const data = {
      userId: consultant.userId
    }
    await axios.get("/sche", data)
      .then((res) => {
        dispatch(changeCsltTimes(res.data))
      })
      .catch((err) => {
      })
    navigate("/consulting/reservation")
  }

  useEffect(() => {
    if (consultant.userPic) {
      setImgsrc("/profile/" + consultant.userPic)
    }
  }, [])

  return (
    <div className={styles.csltCard}>
      <div className={styles.cardBox}>
        <div className={styles.profileBox}>
            {consultant && consultant.userPic !== null ?
              <img className={styles.profileImage} src={process.env.PUBLIC_URL + "/assets/profile/" + (consultant.userPic !== null ? consultant.userPic : 'user.png')} alt="" />
              :
              <img className={styles.profileImage} src={process.env.PUBLIC_URL + "/assets/user.png"} alt="" />
            }
        </div>
        <div className={styles.cardInfo}>
          <div className={styles.nameAndExp}>
            <p className={styles.userName}>{consultant.userName}</p>
            {consultant.csltExp <= 3 && <p className={styles.exp}>수련 치료사</p>}
            {consultant.csltExp > 3 && consultant.csltExp <= 5 && <p className={styles.exp}>전문 치료사</p>}
            {consultant.csltExp > 5 && consultant.csltExp <= 10 && <p className={styles.exp}>프로 치료사</p>}
            {consultant.csltExp > 10 && <p className={styles.exp}>마스터 치료사</p>}
          </div>
          <div className={styles.team}>
            <p>{consultant.csltTeam}</p>
          </div>
          <div className={styles.cardBoxBtm}>
            <div className={styles.boundary}>
              <div>
                <p className={styles.boundaryTitle}><b>치료 가능 영역</b></p>
              </div>
              <div>
                {
                  consultant.csltBoundary.map((boundary, index) => {
                    return (
                      <span key={index}  className={styles.invBoundary}>#{boundary}&nbsp;</span>
                    )
                  })
                }
              </div>
            </div>
            <div className={styles.tag}>
              <div>
                <p className={styles.tagTitle}><b>성향</b></p>
              </div>
              <div>
                {
                  consultant.csltTag.map((tag, index) => {
                    return (
                      <span key={index} className={styles.invTag}>#{tag}&nbsp;</span>
                    )
                  })
                }
              </div>
            </div>
          </div>
          <div>
            <button onClick={goRev} className={styles.btn1}>상담 신청</button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ConsultantCard